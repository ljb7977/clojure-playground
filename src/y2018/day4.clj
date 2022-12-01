(ns y2018.day4
  (:require [util]))

;(def input-val (util/read-lines "y2018/day4.txt"))
(def input-val (util/read-lines "y2018/day4-example.txt"))
(def pattern #"\[(\d+)-(\d+)-(\d+) (\d+):(\d+)\] (wakes up|falls asleep|Guard #(\d+) begins shift)")

; parse-long을 한 번에 할 수 있는 방법이 없을까?
(defn ->action-record-map [[year month day hour minute action guard-id]]
  {:year     (parse-long year)
   :month    (parse-long month)
   :day      (parse-long day)
   :hour     (parse-long hour)
   :minute   (parse-long minute)
   :action   action
   :guard-id (if-let [guard-id guard-id] (parse-long guard-id) nil)})

(defn parse
  "문제의 입력을 파싱하여 action 하나의 정보를 담는 map으로 만듭니다.
  Input: [1518-02-06 23:51] Guard #2081 begins shift
  Output: {:year 1518 :month 2 :day 6 :hour 23 :minute 51 :action \"Guard #2081 begins shift\" :guard-id 2081}

  Input: [1518-05-12 00:39] wakes up
  Output: {:year 1518 :month 5 :day 12 :hour 0 :minute 39 :action \"wakes up\" :guard-id nil}
  "
  [f]
  (->> f
       (re-seq pattern)
       first
       rest
       ->action-record-map))

(defn records->sleeping-ranges
  "위에서 parsing한 map 리스트를 가지고, 잠에 든 시간부터 일어나는 시간까지를 한 묶음으로 묶어 줍니다.
  Input: {:result [] :curr {}}, {:action \"Guard #2081 begins shift\" :month 2 :day 6 :hour 23 :minute 51 :guard-id 2081}
  Output: {:result [] :curr {:guard-id 2081}}

  Input: {:result [] :curr {:guard-id 2081}}, {:action \"falls asleep\" :month 2 :day 7 :hour 0 :minute 1 :guard-id nil}
  Output: {:result [] :curr {:guard-id 2081 :month 2 :day 7 :start-minute 1}}


  Input: {:result [] :curr {:guard-id 2081 :month 2 :day 7 :start-minute 1}},
         {:action \"wakes up\" :month 2 :day 7 :hour 0 :minute 10 :guard-id nil}
  Output: {:result [{:guard-id 2081 :month 2 :day 7 :start-minute 1 :end-minute 10}]
           :curr {:guard-id 2081 :month 2 :day 7 :start-minute 1 :end-minute 10}}
  "
  [{:keys [result curr]}
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
  "
  가드의 id -> 그 가드가 잠들었던 분(minute)들의 리스트 맵을 업데이트합니다.
  Input: {1 '(1 2 3), 2 '(10 11 12)}, {:guard-id 1 :start-minute 6 :end-minute 10}
  Output: {1 '(1 2 3 6 7 8 9), 2 '(10 11 12)}
  "
  [minutes {:keys [guard-id start-minute end-minute]}]
  (update minutes guard-id concat (range start-minute end-minute)))

(defn get-frequencies
  "
  Input: [1 '(1 1 2 3)]
  Output: [1 {1 2, 2 1, 3 1}]
  "
  [[id xs]]
  [id (frequencies xs)])

(defn max-val-of-map
  "
  주어진 map의 value 중에서 가장 큰 값을 반환합니다.
  Input: {1 2 2 3 3 4}
  Output: 4
  "
  [m]
  (apply max (vals m)))

(defn find-most-frequent-one
  "주어진 시퀀스에서 가장 빈도가 높은 원소를 구합니다.
  Input: [1 1 2 2 3 3 3]
  Output: 3"
  [xs]
  (->> xs
       frequencies
       (apply max-key #(second %))
       first))

(defn ->tap [x]
  (tap> x)
  x)

(comment
  ; ----- Part 1: 가장 잠을 많이 잔 가드가, 가장 많이 잠들어있던 minute와 그 가드 id의 곱
  (->> input-val
       sort
       (map parse)
       (reduce records->sleeping-ranges {:result [] :curr {}})
       (->tap)
       :result
       (reduce collect-minutes {})
       (apply max-key #(count (second %)))
       (apply (fn [id minutes] (* id (find-most-frequent-one minutes)))))
       ;((fn [[id minutes]] (* id (find-most-frequent-one minutes)))))  어떤 게 더 나을까요?
  
  ; ----- Part 2: 한 minute에 가장 자주 잠들어있던 가드의 id와, 그 해당 minute의 곱
  (->> input-val
       sort
       (map parse)
       (reduce records->sleeping-ranges {:result [] :curr {}})
       :result
       (reduce collect-minutes {})
       (map get-frequencies)
       (apply max-key #(max-val-of-map (second %))) ; 명시적으로 바인딩을 더 잘 해주고 싶은데...
       ((fn [[id minute-map]] (* id (first (apply max-key second (seq minute-map)))))))) ; 여기 함수로 잘 빼줘야 할듯
