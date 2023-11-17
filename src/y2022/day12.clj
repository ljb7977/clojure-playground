(ns y2022.day12)

(def input (slurp "src/y2022/day12.txt"))

(def diffs 
  [[-1 0]
   [1 0]
   [0 -1]
   [0 1]])

(defn add-point
  "두 점의 좌표를 더하는 함수"
  [[x1 y1] [x2 y2]]
  [(+ x1 x2) (+ y1 y2)])

(defn parse 
  "입력을 파싱하여 정수의 2차원 행렬로 바꿔 주는 함수. \S와 \E는 나중에 찾기 편하기 위해 편의상 -1과 26으로 만들고, 나머지 소문자는 0~25의 값을 가지도록 한다"
  [input-str]
  (let [matrix (->> (clojure.string/split-lines input-str)
                    (map vec))]
    (vec (for [line matrix]
           (vec (for [c line]
                  (case c
                    \S -1
                    \E 26
                    (- (int c) (int \a)))))))))


(defn bfs [{:keys [visited endpoint matrix queue distance] :as state}]
  (if (or (empty? queue) (= (first queue) endpoint))
    (assoc state :stop? true)
    (let [current (first queue)
          next-points-to-visit
          (for [diff diffs
                :let [next-point (add-point current diff)
                      curr-elevation (get-in matrix current)
                      next-elevation (get-in matrix next-point)]
                :when (and curr-elevation next-elevation
                           (<= (- next-elevation curr-elevation) 1)
                           (not (visited next-point)))]
            next-point)
          new-queue (-> (rest queue)
                        (concat next-points-to-visit))
          curr-distance (get distance current 0)
          new-distance (reduce (fn [acc p]
                                 (assoc acc p (+ 1 curr-distance))) distance next-points-to-visit)
          new-visited (apply conj visited next-points-to-visit)]
      (-> state
          (assoc :visited new-visited)
          (assoc :queue new-queue)
          (assoc :distance new-distance)))))
  
(defn get-index [matrix value]
  (first (for [[row-i row] (map-indexed vector matrix)
               [col-i val] (map-indexed vector row)
               :when (= val value)]
           [row-i col-i])))
(comment
  (- (int \z) (int \a))
  (let [sample-input "Sabqponm\nabcryxxl\naccszExk\nacctuvwj\nabdefghi" 
        matrix (parse input)
        start (get-index matrix -1)
        end (get-index matrix 26)
        result (->> (iterate bfs {:matrix matrix
                                  :visited #{start}
                                  :endpoint end
                                  :queue [start]})
                    (drop-while (complement :stop?))
                    (take 1)
                    first)]
    (get (:distance result) end)))
