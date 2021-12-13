(ns y2020.day4
  (:require util))

(def input-val
  (util/read-lines "y2020/day4-example.txt"))
  ;(util/read-lines "y2020/day4.txt"))

(def valid-passport-keys
  #{:byr :iyr :eyr :hgt :hcl :ecl :pid})

(defn group-strings [xs]
  (->> xs
       (reduce (fn [{:keys [result prev]} curr]
                 (if (= "" curr) {:result (conj result prev) :prev ""}
                                 {:result result :prev (str prev curr " ")}))
               {:result [] :prev ""})
       :result))

(defn ->passport-map [xs]
  (->> xs
       (map #(clojure.string/split % #":"))
       (map (fn [[k v]] [(keyword k) v]))
       (into (sorted-map))))
(->passport-map ["hcl:#bc352c" "pid:321838059" "byr:1930" "hgt:178cm" "cid:213" "eyr:2023" "ecl:amb" "iyr:2017"])

(defn validate-passport [m]
  (every? (set (keys m)) valid-passport-keys))


(comment
  (->> input-val
       group-strings
       (map #(clojure.string/split % #" "))
       (map ->passport-map)))
