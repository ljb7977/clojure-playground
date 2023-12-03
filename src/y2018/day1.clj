(ns y2018.day1
  (:require [util]))

(def input-val (util/read-lines "y2018/day1.txt"))

; -------- Part 1 ----------
(comment
  (->> input-val
       (map parse-long)
       (apply +)))

; -------- Part 2 ----------
; version 1
(defn iter [xs]
  (loop [history #{0}
         sum 0
         xs xs]
    (let [addend (first xs)
          sum (+ sum addend)]
      (if (contains? history sum)
        sum
        (recur (conj history sum) sum (rest xs))))))

(comment
  (->> input-val
       (map parse-long)
       cycle
       iter))

; version 2: using reduce
; loop-recur == reduce
(defn find-first-duplicated-number [xs]
  (reduce (fn [visited val]
            (if (visited val) (reduced val) (conj visited val)))
          #{0}
          xs))

(comment
  (->> input-val
       (map parse-long)
       cycle
       (reductions +)
       find-first-duplicated-number))
