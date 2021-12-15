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

(defn get-manhattan-distance [[x1 y1] [x2 y2]]
  "두 좌표의 맨해튼 거리를 구합니다.
  Input: [[1 3] [2 1]]
  Output: 3"
  (+ (abs (- x2 x1)) (abs (- y2 y1))))

(defn transpose [xs]
  (apply map vector xs))

(defn find-min-max-coords [coords]
  "좌표 리스트를 받아서 가장 바깥 x, y 좌표들을 구합니다.
  Input: [(1 2) (3 4) (100 3) (3 100)]
  Output: {:xmin 1 :ymin 2 :xmax 100 :ymax 100}"
  (let [[xs ys] (transpose coords)]
    {:xmin (apply min xs)
     :ymin (apply min ys)
     :xmax (apply max xs)
     :ymax (apply max ys)}))

(defn get-points-in-border
  "경계선 안쪽에 속하는 점들의 좌표를 리스트로 반환합니다.
  Input: {:xmin 1 :ymin 2 :xmax 4 :ymax 3}
  Output: ([1 2] [1 3] [2 2] [2 3] [3 2] [3 3] [4 2] [4 3])"
  [{:keys [xmin xmax ymin ymax]}]
  (for [x (range xmin (inc xmax))
        y (range ymin (inc ymax))]
    [x y]))

(defn get-distance-to-each-targets
  "
  각 점으로부터 각 target까지의 맨해튼 거리를 구한 map의 리스트를 반환합니다.
  "
  [points targets]
  (for [point points [target-id target-coord] targets]
    {:point     point
     :target-id target-id
     :distance  (get-manhattan-distance point target-coord)}))

(defn keep-closest-targets
  "
  Input:
  [[3 2]
    [{:point [3 2], :target-id 0, :distance 3}
    {:point [3 2], :target-id 1, :distance 6}
    {:point [3 2], :target-id 2, :distance 2}
    {:point [3 2], :target-id 3, :distance 2}
    {:point [3 2], :target-id 4, :distance 5}
    {:point [3 2], :target-id 5, :distance 12}]]
  Output: {:point [3 2] :closest-target-id (2 3)}
  "
  [[point ms]]
  (let [shortest-distance (->> ms
                               (map :distance)
                               (apply min))
        closest-target-id (->> ms
                               (filter #(= (:distance %) shortest-distance))
                               (map :target-id))]
    {:point             point
     :closest-target-id closest-target-id}))

(defn point-at-border? [[x y] {:keys [xmin ymin xmax ymax]}]
  (or (= x xmin) (= y ymin) (= x xmax) (= y ymax)))

(defn sum-distances [[_, ms]]
  (->> ms
       (map :distance)
       (apply +)))

(comment
  (let [border (find-min-max-coords input-val)
        points (get-points-in-border border)
        targets (map-indexed vector input-val)
        points-to-targets (get-distance-to-each-targets points targets)
        point-and-its-closest-target (->> points-to-targets
                                          (group-by :point)
                                          (map keep-closest-targets)
                                          (filter (fn [{:keys [point closest-target-id]}]
                                                    (= 1 (count closest-target-id))))
                                          (map #(update % :closest-target-id first)))
        points-at-border (->> point-and-its-closest-target
                              (filter (fn [{:keys [point]}]
                                        (point-at-border? point border))))
        target-ids-which-grow-infinitely (->> points-at-border
                                              (map :closest-target-id)
                                              set)]
    ;points-to-targets))

    ; Part 1
    (->> point-and-its-closest-target
         (filter (fn [{:keys [closest-target-id]}] (not (target-ids-which-grow-infinitely closest-target-id))))
         (map :closest-target-id)
         frequencies
         (map second)
         (apply max)
         println)

    ; Part 2
    (->> points-to-targets
         (group-by :point)
         (map sum-distances)
         (filter #(< % 10000))
         count
         println)))
