(ns vi.script
  (:require [hato.client :as hc]
            [clojure.string]
            [jsonista.core :as j]))
  
(comment
  (let [input "대구보건대학교\n동원과학기술대학교\n계명문화대학교\n청암대학교\n가톨릭상지대학교\n경북과학대학교"]
    #_(count (clojure.string/split-lines input))
    (->> (clojure.string/split-lines input)
         (map #(str "'" % "'"))
         (clojure.string/join ", ")))
  
  (def data 
    (doall (for [i (range 1 22)]
             (let [request-params {:pageIndex i
                                   :pageSize 10
                                   :nppCdLst []
                                   :regionCdLst []
                                   :univKindLst []
                                   :eiName ""
                                   :langCode "ko"}
                   data (hc/post
                          "https://www.studyinkorea.go.kr/ko/study/srchCollegeAjax.do"
                          {:form-params {:jsonStr
                                         (j/write-value-as-string request-params)}})]
               (-> (:body data)
                   (java.net.URLDecoder/decode "UTF-8")
                   (j/read-value))))))
  (let [names (->> data
                   flatten
                   (map #(get % "eiName"))
                   (map #(str "'" % "'")))]
    (as-> (clojure.string/join ", " names) $
          (str "(" $ ")")))
  ,)
