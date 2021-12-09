(ns y2018.day4
  (:require [util]))

(def input-val
  (util/read-lines "y2018/day4.txt"))
(def pattern #"\[(\d+)-(\d+)-(\d+) (\d+):(\d+)\] (wakes up|falls asleep|Guard #(\d+) begins shift)")

; Integer/parseInt 한번에 할 수 있는 방법이 없을까?
(defn to-map [[year month day hour minute action guard-id]]
  {:year     (Integer/parseInt year)
   :month    (Integer/parseInt month)
   :day      (Integer/parseInt day)
   :hour     (Integer/parseInt hour)
   :minute   (Integer/parseInt minute)
   :action   action
   :guard-id (if (nil? guard-id) nil (Integer/parseInt guard-id))})

(defn parse [f]
  (->> f
       (re-seq pattern)
       flatten
       rest
       to-map))

; 자료구조가 불안불안하다...
(defn records->sleeping-ranges [{:keys [result curr]}
                                {:keys [action month day minute guard-id]}]
  (let [updated-curr (cond
                       (not (nil? guard-id)) {:guard-id guard-id}
                       (= action "falls asleep") {:guard-id     (curr :guard-id)
                                                  :month        month
                                                  :day          day
                                                  :start-minute minute}
                       (= action "wakes up") {:guard-id     (curr :guard-id)
                                              :month        (curr :month)
                                              :day          (curr :day)
                                              :start-minute (curr :start-minute)
                                              :end-minute   minute})]
    (if (contains? updated-curr :end-minute)
      {:result (conj result updated-curr) :curr updated-curr}
      {:result result :curr updated-curr})))

(defn collect-minutes
  "Input: {1 '(1 2 3), 2 '(10 11 12)}, {:guard-id 1 :start-minute 6 :end-minute 10}
  Output: {1 (1 2 3 6 7 8 9), 2 (10 11 12)}
  "
  [minutes {:keys [guard-id start-minute end-minute]}]
  (update minutes guard-id concat (range start-minute end-minute)))

(defn get-frequencies [[id xs]]
  [id (frequencies xs)])

(defn max-val-of-map [m]
  (apply max (vals m)))

(comment
  ; ----- Part 1
  (->> input-val
       sort
       (map parse)
       (reduce records->sleeping-ranges {:result [] :curr {}})
       :result
       (reduce collect-minutes {})
       (apply max-key #(count (second %)))
       ((fn [[id minutes]]
         (* id (->> minutes
                   frequencies
                   (apply max-key #(second %))
                   first)))))
  ; ----- Part 2
  (->> input-val
       sort
       (map parse)
       (reduce records->sleeping-ranges {:result [] :curr {}})
       :result
       (reduce collect-minutes {})
       (map get-frequencies)
       (apply max-key #(max-val-of-map (second %)))
       ((fn [[id minute-map]]
          (* id (first (apply max-key second (seq minute-map))))))))
