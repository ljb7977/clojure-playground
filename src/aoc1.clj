(ns aoc1
  (:require [util]))

(def input-val (util/read-lines "aoc1.txt"))

(defn parse-int [string]
  (Integer/parseInt string))

; -------- Part 1 ----------
(->> input-val
     (map parse-int)
     (reduce +))

; -------- Part 2 ----------
; version 1
(defn iter [seq]
  (loop [history #{0}, sum 0, seq seq]
    (let [addend (first seq)
          sum (+ sum addend)]
      (if (contains? history sum)
        sum
        (recur (conj history sum) sum (rest seq))))))

(comment
  (->> input-val
       (map parse-int)
       cycle
       iter))

; version 2: using reduce
; loop-recur == reduce
(defn iter2 [xs]
  (reduce (fn [history, val]
              (if (contains? history val)
                (reduced val)
                (conj history, val)))
          #{0}
          xs))

(comment
  (->> input-val
       (map parse-int)
       cycle
       (reductions +)
       iter2))
