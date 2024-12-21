(ns y2024.day1)

(def input (slurp "src/y2024/day1.txt"))

(comment
  (+ 1 2))

; Part 1
(def solution1
  (let [lines (clojure.string/split-lines input)
        [seq1, seq2] (->> lines
                          (map #(clojure.string/split % #"\s+"))
                          (map #(map parse-long %))
                          (apply mapv vector))
        [seq1, seq2] [(sort seq1) (sort seq2)]]
    (->> (mapv vector seq1 seq2)
         (map (fn [[x y]] (- y x)))
         (map abs)
         (apply +))))
