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
    (:dir :file) (let [path (conj current-dir (:name val))]
                   {:current-dir current-dir
                    :file-tree (assoc-in file-tree path {:info val})})))

(defn walk-calc-size [file-tree]
  (postwalk (fn [node]
              (if (not (:info node)) ; info가 없으면 더 밑으로 내려가면 안 됨
                node
                (case (get-in node [:info :type])
                  :file node
                  :dir (let [children (-> node
                                          (dissoc :info)
                                          vals)
                             size (->> children
                                       (map #(get-in % [:info :size]))
                                       (apply +))]
                          (assoc-in node [:info :size] size))
                  (throw (ex-info "unknown" {:node node})))))
            file-tree))

(defn collect-dir-sizes [tree]
  (case (get-in tree [:info :type])
    :file []
    :dir (let [children (-> tree
                            (dissoc :info)
                            vals)
               size (get-in tree [:info :size])]
           (conj (mapcat collect-dir-sizes children) size))
    (throw (ex-info "no matching clause" {:node tree
                                          :type (get-in tree [:info :type])}))))

(defn parse-input [input]
  (->> (clojure.string/split-lines input)
       (map parse-line)
       (reduce scan-command-result {:current-dir []
                                    :file-tree {"/" {:info {:type :dir
                                                            :name "/"}}}})
       :file-tree))

(comment
  {:type :cd, :dirname "/"}
  {:type :ls}
  {:type :dir, :name "a"}
  {:type :file, :name "b.txt", :size 14848514}
  {:type :file, :name "c.dat", :size 8504156}
  {:type :dir, :name "d"}
  {:type :cd, :dirname "a"}
  
  (scan-command-result {:current-dir [] :file-tree {}}
                       {:type :cd, :dirname "/"})
  (scan-command-result {:current-dir ["/"], :file-tree {}} 
                       {:type :ls})
  (scan-command-result {:current-dir ["/"], :file-tree {"/" {}}}
                       {:type :dir, :name "a"})
  (scan-command-result {:current-dir ["/"], :file-tree {"/" {"a" {:type :dir, :name "a"}}}}
                       {:type :file, :name "b.txt", :size 14848514})
  
  (let [file-tree (parse-input input)
        file-tree-with-size (walk-calc-size file-tree)
        dir-sizes (-> file-tree-with-size
                      (get "/")
                      collect-dir-sizes)]
    (->> dir-sizes
         (filter #(<= % 100000))
         (apply +)))
  := 1581595)

;; Part 2
(comment
  (let [file-tree (parse-input input)
        file-tree-with-size (walk-calc-size file-tree)
        root-dir (get file-tree-with-size "/")
        size-of-root-dir (get-in root-dir [:info :size])
        unused-space (- 70000000 size-of-root-dir)
        required-space (- 30000000 unused-space)  ;; 얘보다 큰 애들 중에 가장 작은 애를 뽑는다
        dir-sizes (-> file-tree-with-size
                      (get "/")
                      collect-dir-sizes)]
    (->> dir-sizes
         (filter #(> % required-space))
         (apply min)))
  := 1544176)
