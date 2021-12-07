(ns aoc1
  (:require [util]))

(def input-val (util/read-input "aoc1.txt"))

(defn parse-int [str]
  (Integer/parseInt str))

(reduce + (map parse-int input-val))
