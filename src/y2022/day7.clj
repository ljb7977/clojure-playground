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

(defn update-size-for-dir
  "node의 info가 없거나, type이 dir이 아니면 (즉, file이면) 더 밑으로 내려가면 안 되고, 그냥 자기 자신을 반환한다.
  node의 type이 dir이면, 모든 children의 size를 합하여 자신의 size에 업데이트한다"
  [node]
  (if (= (get-in node [:info :type]) :dir)
    (let [children (-> node (dissoc :info) vals)
          size (->> children
                    (map #(get-in % [:info :size]))
                    (apply +))]
      (assoc-in node [:info :size] size))
    node))

(defn collect-dir-sizes [node]
  (case (get-in node [:info :type])
    :file []
    :dir (let [children (-> node (dissoc :info) vals)
               size (get-in node [:info :size])]
           (conj (mapcat collect-dir-sizes children) size))
    (throw (ex-info "no matching clause" {:node node
                                          :type (get-in node [:info :type])}))))

(defn parse-input [input]
  (->> (clojure.string/split-lines input)
       (map parse-line)
       (reduce scan-command-result {:current-dir []
                                    :file-tree {"/" {:info {:type :dir
                                                            :name "/"}}}})
       :file-tree))

(comment
  (scan-command-result {:current-dir [] :file-tree {}}
                       {:type :cd, :dirname "/"})
  (scan-command-result {:current-dir ["/"], :file-tree {}} 
                       {:type :ls})
  (scan-command-result {:current-dir ["/"], :file-tree {"/" {}}}
                       {:type :dir, :name "a"})
  (scan-command-result {:current-dir ["/"], :file-tree {"/" {"a" {:type :dir, :name "a"}}}}
                       {:type :file, :name "b.txt", :size 14848514})
  
  ;; Part 1
  (let [file-tree (parse-input input)
        file-tree-with-size (postwalk update-size-for-dir file-tree)
        dir-sizes (-> file-tree-with-size
                      (get "/")
                      collect-dir-sizes)]
    (->> dir-sizes
         (filter #(<= % 100000))
         (apply +)))
  := 1581595
  
  ;; Part 2
  (let [file-tree (parse-input input)
        file-tree-with-size (postwalk update-size-for-dir file-tree)
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
