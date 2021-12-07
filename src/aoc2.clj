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

(defn multiply [[a b]]
  (* a b))

(->> input-val
     (map count-chars)
     (map (juxt
            exists-any-letter-appears-2x?
            exists-any-letter-appears-3x?))
     (apply map +)
     multiply)
; ------- Part 2 -------
