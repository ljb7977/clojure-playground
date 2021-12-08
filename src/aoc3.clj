(ns aoc3
  (:require util))

(def input-val (util/read-lines "aoc3.txt"))
(def pattern #"#(\d+) @ (\d+).(\d+): (\d+)x(\d+)")
(def example-line "#1247 @ 439,612: 25x27")

(defn input-list->map [[idx x y x-size y-size]]
  {:idx    idx
   :x      x
   :y      y
   :x-size x-size
   :y-size y-size})

(defn parse-one-line
  "입력의 한 줄을 {:idx :x :y :x-size :y-size}의 키를 가지는 맵으로 파싱합니다.
  Example: #1 @ 912,277: 27x20
  Result: {:idx 1, :x 912, :y 277, :dx 27, :dy 20}
  "
  [line]
  (->> line
       (re-seq pattern)
       flatten
       rest
       (map util/parse-int)
       input-list->map))


(defn get-covered-points [{x :x y :y x-size :x-size y-size :y-size}]
  (for [dx (range x-size) dy (range y-size)] [(+ x dx) (+ y dy)]))

(defn draw [f]
  (let [idx (:idx f)]
    (reduce (fn [acc [x y]]
              (assoc acc [x y] (conj (get acc [x y] []) idx)))
            {}
            (get-covered-points f))))


(defn process-one-step [f]
  (->> f
       (re-seq pattern)
       flatten
       rest
       (map util/parse-int)
       input-list->map
       draw))

(comment
  (->> input-val
       (map process-one-step)
       (reduce #(merge-with into %1 %2) {})
       vals
       (filter (fn [f] (> (count f) 1)))
       count))

;------- Part 1 끝 -------
