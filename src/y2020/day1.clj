(ns y2020.day1
  (:require util
            [clojure.math.combinatorics :as combo]))

(def input-val
  (util/read-lines "y2020/day1.txt"))

(defn combo-n [n xs]
  (combo/combinations xs n))

(comment
  ; Part 1
  (->> input-val
       (map #(Integer/parseInt %))
       (combo-n 2)
       (filter #(= 2020 (apply + %)))
       first
       (apply *))

  ; Part 2
  (->> input-val
       (map #(Integer/parseInt %))
       (combo-n 3)
       (filter #(= 2020 (apply + %)))
       first
       (apply *)))
