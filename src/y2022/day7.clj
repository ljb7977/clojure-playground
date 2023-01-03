(ns y2022.day7
  (:require [clojure.core.match :refer [match]]
            [clojure.walk :refer [postwalk]]))

(def input (slurp "src/y2022/day7.txt"))

(def sample-input
  "$ cd /\n$ ls\ndir a\n14848514 b.txt\n8504156 c.dat\ndir d\n$ cd a\n$ ls\ndir e\n29116 f\n2557 g\n62596 h.lst\n$ cd e\n$ ls\n584 i\n$ cd ..\n$ cd ..\n$ cd d\n$ ls\n4060174 j\n8033020 d.log\n5626152 d.ext\n7214296 k")

(defn parse-line [line]
  (match (clojure.string/split line #" ")
    (["$" "cd" dirname] :seq) {:type :cd
                               :dirname dirname}
    (["$" "ls"] :seq) {:type :ls}
    (["dir" name] :seq) {:type :dir
                         :name name}
    ([size filename] :seq) {:type :file
                            :name filename
                            :size (parse-long size)}))

(comment
  (parse-line "$ cd /")
  (parse-line "$ ls")
  (parse-line "123124 a.jpg")
  (parse-line "dir hello"))


(defn scan-command-result [{:keys [current-dir file-tree] :as state} val]
  (case (:type val)
    :cd (let [{:keys [dirname]} val]
          (if (= dirname "..")
            {:current-dir (pop current-dir)
             :file-tree file-tree}
            {:current-dir (conj current-dir dirname)
             :file-tree file-tree}))
    :ls {:current-dir current-dir
         :file-tree file-tree}
    :dir {:current-dir current-dir
          :file-tree (assoc-in file-tree (conj current-dir (:name val)) val)}
    :file {:current-dir current-dir
           :file-tree (assoc-in file-tree (conj current-dir (:name val)) val)}))

(defn walk-calc-size [file-tree]
  (postwalk (fn [sub-file-tree]
              (if (not (map? sub-file-tree))
                sub-file-tree
                (case (:type sub-file-tree)
                  :file sub-file-tree
                  :dir (let [size (->> sub-file-tree
                                       vals
                                       (filter map?)
                                       (map :size)
                                       (apply +))]
                         (assoc sub-file-tree :size size))
                  sub-file-tree)))
                  ;(do (prn sub-file-tree)
                  ;    sub-file-tree))))
            file-tree))

(defn reap-sizes-of-dirs [{}])

(comment
  {:type :cd, :dirname "/"}
  {:type :ls}
  {:type :dir, :name "a"}
  {:type :file, :name "b.txt", :size 14848514}
  {:type :file, :name "c.dat", :size 8504156}
  {:type :dir, :name "d"}
  {:type :cd, :dirname "a"}
  
  (scan-command-result {:current-dir []
                        :file-tree {}}
                       {:type :cd, :dirname "/"})
  (scan-command-result {:current-dir ["/"], :file-tree {}} 
                       {:type :ls})
  (scan-command-result {:current-dir ["/"], :file-tree {"/" {}}}
                       {:type :dir, :name "a"})
  (scan-command-result {:current-dir ["/"], :file-tree {"/" {"a" {:type :dir, :name "a"}}}}
                       {:type :file, :name "b.txt", :size 14848514})
  
  (let [size-calced (->> (clojure.string/split-lines sample-input)
                         (map parse-line)
                         (reduce scan-command-result {:current-dir []
                                                      :file-tree {"/" {:type :dir
                                                                       :name "/"}}})
                         :file-tree
                         walk-calc-size)]
    (get size-calced "/")))
;