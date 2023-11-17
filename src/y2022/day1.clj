(ns y2022.day1)

(def input (slurp "src/y2022/day1.txt"))

(def input "1000\n2000\n3000\n\n4000\n\n5000\n6000\n\n7000\n8000\n9000\n\n10000")

(defn parse-input [input]
  (->> (clojure.string/split input #"\n\n")
       (map clojure.string/split-lines)
       (map #(map parse-long %))))

(comment
  (->> (clojure.string/split input #"\n\n")
       (map clojure.string/split-lines)
       (map (fn [x] (map parse-long x)))
       (map (fn [x] (reduce + x)))
       (apply max))
       
  (max 1 2 3)
  (apply max [1 2 3])
  
  (map (fn [x] (map parse-long x)) (map clojure.string/split-lines))
  
  ;; Part 1
  (let [input-clustered (parse-input input)]
    (->> input-clustered
         (map #(apply + %))
         (apply max)))
  ; => 69281

  ;; Part 2
  (let [input-clustered (parse-input input)]
    (->> input-clustered
         (map #(apply + %))
         (sort >)
         (take 3)
         (apply +)))
  :rcf)
