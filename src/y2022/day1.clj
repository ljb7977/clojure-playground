(ns y2022.day1)

(def input (slurp "src/y2022/day1.txt"))

(defn parse-input [input]
  (->> (clojure.string/split input #"\n\n")
       (map clojure.string/split-lines)
       (map #(map parse-long %))))

(comment
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
