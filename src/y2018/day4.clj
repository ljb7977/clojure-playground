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

(defn records->sleeping-ranges [{result :result
                                 curr    :curr}
                                {new-action   :action
                                 new-month    :month
                                 new-day      :day
                                 new-minute   :minute
                                 new-guard-id :guard-id}]
  (let [updated-curr (cond ;get updated curr
                       (not (nil? new-guard-id)) {:guard-id new-guard-id}
                       (= new-action "falls asleep") {:guard-id     (curr :guard-id)
                                                      :month        new-month
                                                      :day          new-day
                                                      :start-minute new-minute}
                       (= new-action "wakes up") {:guard-id     (curr :guard-id)
                                                  :month        (curr :month)
                                                  :day          (curr :day)
                                                  :start-minute (curr :start-minute)
                                                  :end-minute   new-minute})]
    (if (contains? updated-curr :end-minute)
      {:result (conj result updated-curr) :curr updated-curr}
      {:result result :curr updated-curr})))

(defn collect-minutes [result {guard-id :guard-id
                               start :start-minute
                               end :end-minute}]
  (update result guard-id concat (range start end)))

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
