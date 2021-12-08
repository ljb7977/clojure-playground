(ns aoc2
  (:require util))

(def input-val (util/read-lines "aoc2.txt"))

; ------- Part 1 -------
(defn count-chars [f]
  (reduce (fn [counted f1]
            (assoc counted f1 (inc (get counted f1 0))))
          {}
          f))

(defn exists-any-letter-appears-n-times? [m times]
  (if (contains? (set (vals m)) times) 1 0))

(defn exists-any-letter-appears-2x? [m]
  (exists-any-letter-appears-n-times? m 2))

(defn exists-any-letter-appears-3x? [m]
  (exists-any-letter-appears-n-times? m 3))

(comment
  (->> input-val
       (map count-chars)
       (map (juxt
              exists-any-letter-appears-2x?
              exists-any-letter-appears-3x?))
       (apply map +)
       (apply *)))

; ------- Part 2 -------
(defn get-permutation [xs]
  (for [a xs, b xs] [a b]))

(defn get-hamming-distance [f g]
  "두 string f, g간의 해밍 거리를 구합니다.
  Example: asdf, asff -> 1
           abcde, abdcb -> 2
  "
  (reduce (fn [distance, [f1 g1]]
            (if (= f1 g1) distance (inc distance)))
          0
          (map vector f g)))

(defn hamming-distance-1? [[a b]]
  (= 1 (get-hamming-distance a b)))

; 이거 함수로 묶지 말고 펼치는게 나을수도?
(defn find-correct-box-pair [box-pairs]
  (->> box-pairs
       (filter hamming-distance-1?)
       first))

(defn get-common-letters [f g]
  (reduce (fn [commons, [f1 g1]]
            (if (= f1 g1) (str commons f1) commons))
          ""
          (map vector f g)))

(comment
  (->> input-val
       get-permutation
       find-correct-box-pair
       (apply get-common-letters)))
