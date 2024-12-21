(ns y2024.day1)

(def input (slurp "src/y2024/day1.txt"))

(defn parse-input [input]
  (->> (clojure.string/split-lines input)
       (map #(clojure.string/split % #"\s+"))
       (map #(map parse-long %))
       (apply mapv vector))) 

; Part 1
(def solution1
  (let [[seq1, seq2] (parse-input input) 
        [seq1, seq2] [(sort seq1) (sort seq2)]]
    (->> (mapv vector seq1 seq2)
         (map (fn [[x y]] (- y x)))
         (map abs)
         (apply +))))

; Part 2
(defn counter [xs]
  (reduce (fn [acc val]
            (let [count (get acc val)]
              (if (some? count)
                (assoc acc val (inc count))
                (assoc acc val 1)))) {} xs))

(def solution2
  (let [[seq1 seq2] (parse-input input)
        count-map (counter seq2)]
    (->> seq1
         (map (fn [x] (get count-map x 0)))
         (map (fn [x y] (* x y)) seq1)
         (apply +))))