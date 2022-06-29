/*
  Framework : program 생성 시 기본 틀 제공  (개발자 호출 X, 시스템이 호출)
  Library : program 작성 시 필요한 기능 가져다 쓸 수 있는 기능 모음집   (개발자가 호출함)

  Segue : 화면마다 연결된 기능 명시적으로 선으로 연결함

  멤버변수 선언 시 초기화 필수 / 지역변수는 초기화 필수 아님
*/

import UIKit

class ViewController: UIViewController, EditDelegate, DateDelegate {
  
    // EditViewController에 선언한 protocal 구현함.
    
    @IBOutlet var txMessage: UITextField!
    @IBOutlet var imgView: UIImageView!
    @IBOutlet var lblDate: UILabel!
    
    let imgOn = UIImage(named: "lamp-on.png")
    let imgOff = UIImage(named: "lamp-off.png")
    
    var isOn = true
    var isZoom = false
    var isZoomOut = false
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        
        imgView.image = imgOn
        lblDate.text = " "
    }

    // 화면 전환 시 자동으로 호출되는 함수 (세그웨이 방향이 정방향일 때 사용)
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // segue.destination as! 세그웨이 Identifier
        // as : Typecasting이라고 생각. (목적지를 identifier로 타입 캐스팅 해서 editViewController 변수에 넣음)
        if segue.identifier == "dateButton" {
            let dateViewController = segue.destination as! DatePickerViewController
            dateViewController.dateDelegate = self
        }
        else {
            let editViewController = segue.destination as! EditViewController
            
            if segue.identifier == "editButton" {
                // textWayValue는 멤버 변수임
                editViewController.textWayValue = "seque : use button"
            } else if segue.identifier == "editBarButton" {
                editViewController.textWayValue = "seque : use Bar button"
            }
            
            editViewController.textMessage = txMessage.text!
            editViewController.isOn = isOn
            editViewController.isZoom = isZoom
            editViewController.isZoomOut = isZoomOut
            editViewController.delegate = self  // delegate 역할은 ViewController로 지정
        }
       
    }
    
    func didMessageEditDone(_ controller: EditViewController, message: String) {
        txMessage.text = message
    }
    
    func didImageOnOffDone(_ controller: EditViewController, isOn: Bool) {
        if isOn {
            imgView.image = imgOn
            self.isOn = true
        } else {
            imgView.image = imgOff
            self.isOn = false
        }
    }
    
    func didImageZoomDone(_ controller: EditViewController, isZoom: Bool) {
        if isZoom {
            let scale: CGFloat = 2.0 // let은 상수를 선언할 때 사용함
            var newWidth: CGFloat, newHeight: CGFloat
            
            newWidth = imgView.frame.width * scale
            newHeight = imgView.frame.height * scale
            
            // CGSize는 메모리 고려하지 않고 저장 가능
            imgView.frame.size = CGSize(width: newWidth, height: newHeight)
            self.isZoom = true
            self.isZoomOut = false
        }
    }
    
    func didImageZoomOutDone(_ controller: EditViewController, isZoomOut: Bool) {
        if isZoomOut {
            let scale: CGFloat = 2.0 // let은 상수를 선언할 때 사용함
            var newWidth: CGFloat, newHeight: CGFloat
            
            newWidth = imgView.frame.width / scale
            newHeight = imgView.frame.height / scale
            
            // CGSize는 메모리 고려하지 않고 저장 가능
            imgView.frame.size = CGSize(width: newWidth, height: newHeight)
            self.isZoomOut = true
            self.isZoom = false
        }
    }
    
    func didDatePickDone(_ controller: DatePickerViewController, date: String) {
        lblDate.text = date
    }
}

