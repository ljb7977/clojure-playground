(ns aoc2
  (:require util))

(def input-val (util/read-lines "aoc2.txt"))

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
(defn get-permutation [input]
  (for [a input, b input] [a b]))

(defn get-hamming-distance [a b]
  (loop [distance 0, a a, b b]
    (let [_a (first a), _b (first b)]
      (cond
        (or (nil? _a) (nil? _b)) distance
        (= _a _b) (recur distance (rest a) (rest b))
        :else (recur (inc distance) (rest a) (rest b))))))

(defn find-correct-boxes [boxes]
  (let [[a b] (first boxes)]
    (if (= 1 (get-hamming-distance a b))
      [a b]
      (recur (rest boxes)))))

(defn get-common-letters [[a b]]
  (loop [commons "", a a, b b]
    (let [_a (first a), _b (first b)]
      (cond
        (or (nil? _a) (nil? _b)) commons
        (= _a _b) (recur (str commons _a) (rest a) (rest b))
        :else (recur commons (rest a) (rest b))))))

(->> input-val
     get-permutation
     find-correct-boxes
     get-common-letters)
