(ns y2022.day8
  (:require [clojure.string]))

(defn char->int [c]
  (- (int c) (int \0)))

(defn parse-input [input]
  (->> (clojure.string/split-lines input)
       (mapv #(mapv char->int %))))
(def input (parse-input (slurp "src/y2022/day8.txt")))
(def sample-input (parse-input "30373\n25512\n65332\n33549\n35390"))


(defn conj-visible [{:keys [max visibility]} val]
  (if (> val max)
    {:max val
     :visibility (conj visibility true)}
    {:max max
     :visibility (conj visibility false)}))

(defn calc-visibility [xs]
  (->> xs
       (reduce conj-visible {:max -1 :visibility []})
       :visibility))

(defn calc-scenic-score [xs]
  (->> (for [[index x] (map-indexed vector xs)
             :let [sub-xs (subvec xs (inc index))]]
         (reduce (fn [acc val]
                   (if (< val x)
                     (inc acc) ;; x보다 아직 작으면 inc 후 진행
                     (reduced (inc acc)))) ;; x보다 크거나 같으면 inc하고 멈춤.
                 0
                 sub-xs))
       vec))
(defn flip-matrix [mat]
  (->> mat
       (mapv (fn [xs] (->> xs rseq (into []))))))

(defn transpose [mat]
  (apply map vector mat))

(defn apply-f-for-matrix-for-all-directions [f mat]
  (let [left (->> mat
                  (mapv f))
        right (->> mat
                   flip-matrix
                   (mapv f)
                   flip-matrix)
        transposed (transpose mat)
        top (->> transposed
                 (mapv f)
                 transpose)
        bottom (->> transposed
                    flip-matrix
                    (map f)
                    flip-matrix
                    transpose)]
    [left right top bottom]))

(comment
  ;; Part 1
  (let [[l r t b] (apply-f-for-matrix-for-all-directions calc-visibility input)
        [l r t b] (map #(apply concat %) [l r t b])]
    (->> (map (fn [& xs] (some true? xs)) l r t b)
         (filter true?)
         count))
  := 1695

  ;; Part 2
  (let [[l r t b] (apply-f-for-matrix-for-all-directions calc-scenic-score input)
        [l r t b] (map #(apply concat %) [l r t b])]
    (->> (map * l r t b)
         (apply max)))

  := 287040)
