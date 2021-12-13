(ns y2020.day1
  (:require util))

(def input-val
  (util/read-lines "y2020/day1.txt"))

(defn permute-2 [xs]
  (for [x xs, y xs] [x y]))

(defn permute-3 [xs]
  (for [x xs,
        y xs,
        z xs]
    [x y z]))

(defn sum-and-multiply [[x y]]
  [(+ x y) (* x y)])

(defn sum-is-not-2020? [xs]
  (not (= 2020 (apply + xs))))

(comment
  ; Part 1
  (->> input-val
       (map #(Integer/parseInt %))
       permute-2
       (drop-while sum-is-not-2020?)
       first
       (apply *))

  ; Part 2
  (->> input-val
       (map #(Integer/parseInt %))
       permute-3
       (drop-while sum-is-not-2020?)
       first
       (apply *)))
