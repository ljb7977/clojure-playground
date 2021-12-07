(ns aoc1
  (:require [util]))

(def input-val (util/read-input "aoc1.txt"))

(defn parse-int [str]
  (Integer/parseInt str))

(def input-seq (map parse-int input-val))

; -------- Part 1 ----------
(reduce + input-seq)

; -------- Part 2 ----------
(defn iter [history now seq]
  (let [adder (first seq)
        now (+ now adder)]
    (if (contains? history now)
      now
      (recur (conj history now) now (rest seq)))))

(->> input-seq
     cycle
     (iter #{0} 0))
