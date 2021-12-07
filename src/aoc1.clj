(ns aoc1
  (:require [util]))

(def input-val (util/read-input "aoc1.txt"))

(defn parse-int [str]
  (Integer/parseInt str))

(def input-seq (map parse-int input-val))

; -------- Part 1 ----------
(reduce + input-seq)

; -------- Part 2 ----------
(defn iter [seq]
  (loop [history #{0}, sum 0, seq seq]
    (let [addend (first seq)
          sum (+ sum addend)]
      (if (contains? history sum)
        sum
        (recur (conj history sum) sum (rest seq))))))

(->> input-seq
     cycle
     iter)
