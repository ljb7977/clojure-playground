(ns aoc2
  (:require util))

(def input-val (util/read-input "aoc2.txt"))

(defn count-chars [str]
  (loop [str str, counted {}]
    (let [ch (first str)]
      (if (empty? str)
        counted
        (recur (rest str) (assoc counted ch (inc (get counted ch 0))))))))

(defn exists-any-letter-appears-n-times? [m times]
  (if (contains? (set (vals m)) times) 1 0))

(defn exists-any-letter-appears-2x? [m]
  (exists-any-letter-appears-n-times? m 2))

(defn exists-any-letter-appears-3x? [m]
  (exists-any-letter-appears-n-times? m 3))

(defn sum-each [pairs]
  (loop [sum-a 0, sum-b 0, pairs pairs]
    (let [[a b] (first pairs)]
      (if (empty? pairs)
        [sum-a sum-b]
        (recur (+ sum-a a) (+ sum-b b) (rest pairs))))))

(defn multiply [[a b]]
  (* a b))

(->> input-val
     (map count-chars)
     (map (juxt
            exists-any-letter-appears-2x?
            exists-any-letter-appears-3x?))
     sum-each
     multiply)
; ------- Part 2 -------
