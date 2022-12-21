(ns y2022.day4)

(def input (slurp "src/y2022/day4.txt"))

(defn parse-input [line]
  (let [[_ s1 e1 s2 e2] (re-matches #"(\d+)-(\d+),(\d+)-(\d+)" line)
        [s1 e1 s2 e2] (map parse-long [s1 e1 s2 e2])]
    [[s1 e1] [s2 e2]]))

(defn fully-contains? [[[s1 e1] [s2 e2]]]
  (cond
    (and (<= s1 s2) (>= e1 e2)) true
    (and (<= s2 s1) (>= e2 e1)) true
    :else false))

(defn overlaps? [[[s1 e1] [s2 e2]]]
  (cond
    (or (< e2 s1) (< e1 s2)) false
    :else true))

(comment
  (->> (clojure.string/split-lines input)
       (map parse-input)
       (map fully-contains?)
       (filter true?)
       count)

  := 569

  (->> (clojure.string/split-lines input)
       (map parse-input)
       (map overlaps?)
       (filter true?)
       count)

  := 936

  :rcf)
