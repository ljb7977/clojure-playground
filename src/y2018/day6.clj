(ns y2018.day6
  (:require util))

(defn parse [x]
  (let [t (clojure.string/split x #", ")]
    (map #(Integer/parseInt %) t)))

(def input-val
  (->> "y2018/day6.txt"
       util/read-lines
       (map parse)))

(defn abs [x]
  (max x (- x)))

(defn get-distance [[x1 y1] [x2 y2]]
  (+ (abs (- x2 x1)) (abs (- y2 y1))))

(defn find-min-max-coords [coords]
  (let [xs (map first coords)
        ys (map second coords)]
    {:xmin (apply min xs)
     :ymin (apply min ys)
     :xmax (apply max xs)
     :ymax (apply max ys)}))

(defn get-points-in-range [{:keys [xmin xmax ymin ymax]}]
  (for [x (range xmin (inc xmax))
        y (range ymin (inc ymax))]
    [x y]))

(defn get-distance-to-each-coords [locations coords]
  (for [location locations [coord-id coord-point] coords]
    {:location location
     :coord-id coord-id
     :distance (get-distance location coord-point)}))

(defn keep-closest-coords
  "
  Input:
  [[3 2]
    [{:location [3 2], :coord-id 0, :distance 3}
    {:location [3 2], :coord-id 1, :distance 6}
    {:location [3 2], :coord-id 2, :distance 2}
    {:location [3 2], :coord-id 3, :distance 2}
    {:location [3 2], :coord-id 4, :distance 5}
    {:location [3 2], :coord-id 5, :distance 12}]]
  Output: {:location [3 2] :closest-coord-ids (2 3)}
  "
  [[location ms]]
  (let [shortest-distance (->> ms
                               (map :distance)
                               (apply min))
        closest-coord-ids (->> ms
                               (filter #(= (:distance %) shortest-distance))
                               (map :coord-id))]
    {:location          location
     :closest-coord-ids closest-coord-ids}))

(comment
  (let [min-max-points (find-min-max-coords input-val)
        locations (get-points-in-range min-max-points)
        coords (map-indexed vector input-val)
        locations-to-coords (get-distance-to-each-coords locations coords)]
    (->> locations-to-coords
         (group-by :location)
         (map keep-closest-coords)
         (filter (fn [{:keys [location closest-coord-ids]}]
                     (= 1 (count closest-coord-ids))))
         (map :closest-coord-ids)
         (flatten)
         frequencies
         (map second)
         (apply max))))
