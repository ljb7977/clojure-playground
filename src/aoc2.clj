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

(->> input-val
     (map count-chars)
     (map (juxt
            exists-any-letter-appears-2x?
            exists-any-letter-appears-3x?))
     (apply map +)
     (apply *))
; ------- Part 2 -------
(defn get-permutation [xs]
  (for [a xs, b xs] [a b]))

(defn get-hamming-distance [a b]
  "Example: asdf, asff -> 1"
  (loop [distance 0, [first-a & rest-a] a, [first-b & rest-b] b]
      (cond
        (or (nil? first-a) (nil? first-b)) distance
        (= first-a first-b) (recur distance rest-a rest-b)
        :else (recur (inc distance) rest-a rest-b))))

;(defn find-correct-boxes [boxes]
;  (let [[a b] (first boxes)]
;    (if (= 1 (get-hamming-distance a b))
;      [a b]
;      (recur (rest boxes)))))

(defn hamming-distance-1? [[a b]]
  (= 1 (get-hamming-distance a b)))

(defn find-correct-box-pair [box-pairs]
  (->> box-pairs
       (filter hamming-distance-1?)
       first))

(defn get-common-letters [[a b]]
  (loop [commons "", [first-a & rest-a] a, [first-b & rest-b] b]
      (cond
        (or (nil? first-a) (nil? first-b)) commons
        (= first-a first-b) (recur (str commons first-a) rest-a rest-b)
        :else (recur commons rest-a rest-b))))

(comment
  (->> input-val
       get-permutation
       find-correct-box-pair
       get-common-letters))
