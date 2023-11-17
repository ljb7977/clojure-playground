(ns user)

(defn ->tap [x]
  (tap> x)
  x)

(require '[java-time.api :as jt])
(comment
  (def date (jt/local-date 2023 1 20))
  
  (def cookie-string "cf_clearance=sFNB.WwALTxTBTXLf9uW8YtrOtuSi5nY5QDUnWVyigM-1692694040-0-1-a6813b6b.2aaf106a.f180648b-250.0.0; __cf_bm=qQn1Pxdl9rCePUajoFqy4oJtR5VM5lxfsuHoucex4uE-1692774869-0-Abi9af6/U6oZNyYPs2IxW6UWkQWkcGaffLl+RbuYyA2+qXtOfjmisohrtl8fkQH7JzZcd9vjOqyX8zIfAS40bzdbEybzWHDK4v8KXSGnP7IR; grb_dynamic_list@=checked%2C%5B%5D; grb_ip_permission@e72b7fda=success; _ga_BVQGVEDG55=GS1.1.1692775534.1.0.1692775534.60.0.0; grb_ck@e72b7fda=f3630d8b-729b-64a4-6a17-662143876a35; bm_session_id=202308231625_20167048-c96f-430b-b9d6-9563a3e2ceaf/1692775534214; _ceo_v2_gk_sid=d1d31ba2-7a23-4f57-b19a-053f2bd7be82; _hackle_hid=afa59bda-a7e2-42bd-9f30-c724ba0646a5; wcs_bt=642c55d33b747:1692775533; _ga=GA1.1.914863222.1692775534; grb_id_permission@e72b7fda=success; cookie30d=; dsid=874018bf-6ff8-4d5f-b532-9853815818e1; _fbp=fb.1.1692775534113.1016988001; grb_ui@e72b7fda=6ea2d443-6c53-cf58-694f-5d06d1ae4837; _wp_uid=1-7c9551f581e4e422507a653a9d237e4d-s1692775534.251658|etc|chrome-uhp1iy; _gcl_au=1.1.1581997654.1692775534; bsgid=202308231625_20167048-c96f-430b-b9d6-9563a3e2ceaf; _ga_QZ54WQ25KW=GS1.1.1692775534.1.0.1692775534.0.0.0; _ga_DD6D4M7LEB=GS1.1.1692775533.1.0.1692775533.0.0.0")
  (let [cookie-string "cf_clearance=sFNB.WwALTxTBTXLf9uW8YtrOtuSi5nY5QDUnWVyigM-1692694040-0-1-a6813b6b.2aaf106a.f180648b-250.0.0; __cf_bm=qQn1Pxdl9rCePUajoFqy4oJtR5VM5lxfsuHoucex4uE-1692774869-0-Abi9af6/U6oZNyYPs2IxW6UWkQWkcGaffLl+RbuYyA2+qXtOfjmisohrtl8fkQH7JzZcd9vjOqyX8zIfAS40bzdbEybzWHDK4v8KXSGnP7IR; grb_dynamic_list@=checked%2C%5B%5D; grb_ip_permission@e72b7fda=success; _ga_BVQGVEDG55=GS1.1.1692775534.1.0.1692775534.60.0.0; grb_ck@e72b7fda=f3630d8b-729b-64a4-6a17-662143876a35; bm_session_id=202308231625_20167048-c96f-430b-b9d6-9563a3e2ceaf/1692775534214; _ceo_v2_gk_sid=d1d31ba2-7a23-4f57-b19a-053f2bd7be82; _hackle_hid=afa59bda-a7e2-42bd-9f30-c724ba0646a5; wcs_bt=642c55d33b747:1692775533; _ga=GA1.1.914863222.1692775534; grb_id_permission@e72b7fda=success; cookie30d=; dsid=874018bf-6ff8-4d5f-b532-9853815818e1; _fbp=fb.1.1692775534113.1016988001; grb_ui@e72b7fda=6ea2d443-6c53-cf58-694f-5d06d1ae4837; _wp_uid=1-7c9551f581e4e422507a653a9d237e4d-s1692775534.251658|etc|chrome-uhp1iy; _gcl_au=1.1.1581997654.1692775534; bsgid=202308231625_20167048-c96f-430b-b9d6-9563a3e2ceaf; _ga_QZ54WQ25KW=GS1.1.1692775534.1.0.1692775534.0.0.0; _ga_DD6D4M7LEB=GS1.1.1692775533.1.0.1692775533.0.0.0"]
    (->> (clojure.string/split cookie-string #"; ")
         (map (fn [v] (clojure.string/split v #"=")))
         (filter (fn [entry] (= 2 (count entry))))
         (into {}))))
    
