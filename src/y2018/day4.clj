(ns y2018.day4
  (:require [util]))
(require '[clojure.core.match :refer [match]])

(def input-val
  (util/read-lines "y2018/day4.txt"))
(def pattern #"\[(\d+)-(\d+)-(\d+) (\d+):(\d+)\] (wakes up|falls asleep|Guard #(\d+) begins shift)")

(defn to-map [[year month day hour minute action guard-id]]
  {:year     year
   :month    month
   :day      day
   :hour     hour
   :minute   minute
   :action   action
   :guard-id guard-id})

(defn parse [f]
  (->> f
       (re-seq pattern)
       flatten
       rest
       to-map))

; acc
;{:result [{:guard-id :month :day :start-minute :end-minute} ...]
; :curr {:guard-id :month :day :start-minute}}
;
;val
;{:guard-id
; :action
; :month
; :day
; :minute}

(defn process [{result :result
                curr :curr}
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
      {:result (conj result updated-curr) :curr {}}
      {:result result :curr updated-curr})))

(->> input-val
     sort
     (take 10)
     (map parse)
     (reduce process {:result [] :curr {}}))
