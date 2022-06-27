/*
    사용자 위치정보 수집 요청상자 띄우기 설정 셋팅
    ** info.plist 수정
 
    => Privacy - Location When In Use Usage Description -> (Value에 입력) App needs location servers for stuff.
 */

import UIKit
import MapKit

class ViewController: UIViewController, CLLocationManagerDelegate {

    @IBOutlet var myMap: MKMapView!
    @IBOutlet var lblLocationInfo1: UILabel!
    @IBOutlet var lblLocationInfo2: UILabel!
    
    let locationManager = CLLocationManager ()  // LocationManager 객체 생성
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        
        lblLocationInfo1.text = " "
        lblLocationInfo2.text = " "
        locationManager.delegate = self // locationManager가 할 일을 viewController에게 위임한다.
        locationManager.desiredAccuracy = kCLLocationAccuracyBest
        locationManager.requestWhenInUseAuthorization()
        locationManager.startUpdatingLocation() // 위치정보 수집 시작
        myMap.showsUserLocation = true  // 사용자 위치 표시
    }
    
    func goLocation(latitudeValue: CLLocationDegrees, longtitudeValue: CLLocationDegrees, delta span :Double) -> CLLocationCoordinate2D {
        let pLocation = CLLocationCoordinate2DMake(latitudeValue, longtitudeValue)
        let spanValue = MKCoordinateSpan(latitudeDelta: span, longitudeDelta: span)
        let pRegion = MKCoordinateRegion(center: pLocation, span: spanValue)
        myMap.setRegion(pRegion, animated: true)
        return pLocation
    }

    // 실제 주소 알아내기
    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        let pLocation = locations.last
        goLocation(latitudeValue: (pLocation?.coordinate.latitude)!, longtitudeValue: (pLocation?.coordinate.longitude)!, delta: 0.01)
        
        CLGeocoder().reverseGeocodeLocation(pLocation!, completionHandler: {
            (placemarks, error) -> Void in
            let pm = placemarks!.first
            let country = pm!.country
            var address:String = country!
            if pm!.locality != nil {
                address += " "
                address += pm!.locality!
            }
            if pm!.thoroughfare != nil {
                address += " "
                address += pm!.thoroughfare!
            }
            
            self.lblLocationInfo1.text = "현재 위치"
            self.lblLocationInfo2.text = address
        })
        
        locationManager.stopUpdatingLocation()  // 사용자 위치 정보 수집 중지
    }
    
    func setAnnotation(latitudeValue: CLLocationDegrees, longtitudeValue: CLLocationDegrees, delta span :Double, title strTitle: String, subtitle strSubtitle: String){
        let annotation = MKPointAnnotation()
        annotation.coordinate = goLocation(latitudeValue: latitudeValue, longtitudeValue: longtitudeValue, delta: span)
        annotation.title = strTitle
        annotation.subtitle = strSubtitle
        myMap.addAnnotation(annotation)
    }

    @IBAction func sgChangeLocation(_ sender: UISegmentedControl) {
        if sender.selectedSegmentIndex == 0 {
            self.lblLocationInfo1.text = ""
            self.lblLocationInfo2.text = ""
            locationManager.startUpdatingLocation()
        } else if sender.selectedSegmentIndex == 1 {
            setAnnotation(latitudeValue: 37.60737, longtitudeValue: 127.04261, delta: 0.1, title: "동덕여자대학교", subtitle: "서울특별시 성북구 화랑로13길 60")
            self.lblLocationInfo1.text = "보고 계신 위치"
            self.lblLocationInfo2.text = "동덕여자대학교 월곡캠퍼스"
        }else if sender.selectedSegmentIndex == 2 {
            setAnnotation(latitudeValue: 37.590899, longtitudeValue: 127.13465, delta: 0.1, title: "우리집", subtitle: "구리시 장자대로 37번길 70")
            self.lblLocationInfo1.text = "보고 계신 위치"
            self.lblLocationInfo2.text = "송현수 집"
        }
    }
    

}
