(ns y2018.day3
  (:require util))

(def input-val (util/read-lines "y2018/day3.txt"))
(def pattern #"#(\d+) @ (\d+).(\d+): (\d+)x(\d+)")
(def example-line "#1247 @ 439,612: 25x27")

(defn input-list->map
  "idx, x, y, x-size, y-size 순으로 들어오는 리스트에 키워드를 붙여 fabric을 표현하는 맵으로 변환합니다."
  [[idx x y x-size y-size]]
  {:idx    idx
   :x      x
   :y      y
   :x-size x-size
   :y-size y-size})

(defn parse-one-line
  "입력의 한 줄을 {:idx :x :y :x-size :y-size}의 키를 가지는 맵으로 파싱합니다.
  Example)
  Input: #1 @ 912,277: 27x20
  Output: {:idx 1, :x 912, :y 277, :dx 27, :dy 20}
  "
  [line]
  (->> line
       (re-seq pattern)
       flatten
       rest
       (map util/parse-int)
       input-list->map))

(defn get-covered-points
  "x, y, x-size, y-size에 기반하여 해당 fabric이 덮는 점들의 좌표 리스트를 구합니다.
  Example)
  Input: {:x 1 :y 3 :x-size 3 :y-size 2}
  Output: ([1 3] [1 4] [2 3] [2 4] [3 3] [3 4])
  "
  [{x :x y :y x-size :x-size y-size :y-size}]
  (for [dx (range x-size) dy (range y-size)] [(+ x dx) (+ y dy)]))

(defn draw
  "map 형태의 fabric을 입력으로 받아, 좌표평면의 어떤 점이 해당 fabric으로 덮이는지를 표현한 맵을 반환합니다.
  Input: {:idx 1 :x 1 :y 3 :x-size 3 :y-size 2}
  Output: {[1 3] [1], [1 4] [1], [2 3] [1], [2 4] [1], [3 3] [1], [3 4] [1]}
  "
  [f]
  (zipmap (get-covered-points f) (repeat [(:idx f)])))

(defn process-one-step [f]
  (->> f
       (re-seq pattern)
       flatten
       rest
       (map util/parse-int)
       input-list->map
       draw))

;------- Part 1 -------
(comment
  (->> input-val
       (map process-one-step)
       (reduce #(merge-with into %1 %2) {})
       vals
       (filter #(> (count %) 1))
       count))

;------- Part 2 -------
(comment
  (let [num-lines (count input-val)]
    (->> input-val
         (map process-one-step)
         (reduce #(merge-with into %1 %2) {})
         vals
         (filter #(> (count %) 1))
         (reduce (fn [acc val]
                   (apply disj acc val)) (set (range 1 (inc num-lines))))
         first)))
