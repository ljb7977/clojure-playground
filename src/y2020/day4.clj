(ns y2020.day4
  (:require [util]
            [clojure.spec.alpha :as s]))

(def input-val
  (-> (slurp "resources/y2020/day4.txt")
      (clojure.string/split #"\n\n")))

(defn parse-int-if-available [x]
  (if-let [parsed (parse-long x)]
    parsed
    x))

(defn parse-height
  "Input: 180cm
  Output: {:num 180 :unit :cm}

  Input: 200m
  Output: nil
  "
  [s]
  (if
    (nil? s)
    nil
    (when-let [[[_ num unit]] (re-seq #"^(\d+)(cm|in)$" s)]
      {:num (parse-long num)
       :unit (keyword unit)})))

(defn parse-values [{:keys [byr cid ecl eyr
                            hcl hgt iyr pid]
                     :as all}]
  (merge all
         {:byr (parse-int-if-available byr)
          :iyr (parse-int-if-available iyr)
          :eyr (parse-int-if-available eyr)
          :pid pid
          :hcl hcl
          :hgt (parse-height hgt)
          :ecl (keyword ecl)
          :cid cid}))

(defn ->passport-map
  "
  key:value 형태의 string의 sequence로 들어오는 입력값을 map 형태로 변환합니다.
  Input: [\"hcl:#bc352c\" \"pid:321838059\" \"byr:1930\" \"hgt:178cm\" \"cid:213\" \"eyr:2023\" \"ecl:amb\" \"iyr:2017\"]
  Output: {:byr \"1930\", :cid \"213\", :ecl \"amb\", :eyr \"2023\", :hcl \"#bc352c\", :hgt \"178cm\", :iyr \"2017\", :pid \"321838059\"}
  "
  [xs]
  (->> xs
       (map #(clojure.string/split % #":"))
       (map (fn [[k v]] [(keyword k) v]))
       (into (sorted-map))
       parse-values))
;(->passport-map ["hcl:#bc352c" "pid:321838059" "byr:1930" "hgt:178cm" "cid:213" "eyr:2023" "ecl:amb" "iyr:2017"])

(defn parse [x]
  (->> x
       (map #(clojure.string/split % #"\s"))
       (map ->passport-map)))

(def required-keys #{:byr :iyr :eyr :hgt :hcl :ecl :pid})
(defn has-required-keys? [m]
  (every? m required-keys))

(s/def :passport/eye-colors #{:amb :blu :brn :gry :grn :hzl :oth})

(defn valid-height? [{:keys [num unit]}]
  (case unit
     :cm (<= 150 num 193)
     :in (<= 59 num 76)
     false))

(s/def :passport/byr (s/int-in 1920 2003))
(s/def :passport/iyr (s/int-in 2010 2021))
(s/def :passport/eyr (s/int-in 2020 2031))
(s/def :passport/hgt (s/and some? valid-height?))
(s/def :passport/hcl (s/and string? #(re-matches #"^#[a-f|0-9]{6}$" %)))
(s/def :passport/ecl (s/and keyword? #(s/valid? :passport/eye-colors %)))
(s/def :passport/pid (s/and string? #(re-matches #"^\d{9}$" %)))
(s/def :passport/passport (s/keys :req-un [:passport/byr :passport/iyr :passport/eyr :passport/hgt
                                           :passport/hcl :passport/ecl :passport/pid]
                                  :opt-un [:passport/cid]))

(comment
  ; Part 1
  (->> input-val
       parse
       (filter has-required-keys?)
       count)

  ; Part 2
  (->> input-val
       parse
       (filter #(s/valid? :passport/passport %))
       count))
