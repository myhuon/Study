import UIKit

protocol DateDelegate {
    func didDatePickDone(_ controller: DatePickerViewController, date: String)
}

class DatePickerViewController: UIViewController {
    
    /*
        updateTime 함수를 selector로 감싸서 시스템에 callback 함수로 넘겨준다.
     */
    let timeSelector: Selector = #selector(DatePickerViewController.updateTime)
    let interval = 1.0
    
    var count = 0
    var alarmTime = "0"
    var isAlarmTime = false
    var delegate: DateDelegate?
    var pickerTime = ""

    @IBOutlet var uiView: UIView!
    @IBOutlet weak var lblCurrentTime: UILabel!
    @IBOutlet weak var lblPickerTime: UILabel!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        
        /*
            interval 1 => 1 seconds
            1초마다 updateTime 함수(@objc함수)를 수행한다.
         */
        Timer.scheduledTimer(timeInterval: interval, target: self, selector: timeSelector, userInfo: nil, repeats: true)
    }
    
    @IBAction func changeDatePicker(_ sender: UIDatePicker) {
        /*
            let은 상수를 의미한다.
            sender를 상수로 굳이 쓰는 이유는 만약, 실수로 sender를 변경시도를 할 시
            complier 자체에서 에러를 검출하여 코드 에러를 줄이도록 하기 위함이다.
         */
        let datePickerView = sender
        
        let formatter = DateFormatter()
        //formatter.dateFormat = "yyyy-MM-dd HH:mm EEE" // EEE는 요일을 나타냄
        formatter.dateFormat = "hh:mm aaa"
        pickerTime = formatter.string(from: datePickerView.date)
        lblPickerTime.text = "선택시간: " + pickerTime
    
        // 알람기능
        //formatter.dateFormat = "hh:mm:ss aaa"
        alarmTime = formatter.string(from: datePickerView.date)
    }
    
    // callback 함수, @objc는 objective-c를 사용하겠다는 의미.
    @objc func updateTime() {
        /*lblCurrentTime.text = String(count)
        count += 1*/
        
        let date = NSDate()
        
        let formatter = DateFormatter()
        //formatter.dateFormat = "yyyy-MM-dd HH:mm EEE" // EEE는 요일을 나타냄
        formatter.dateFormat = "hh:mm aaa"
        lblCurrentTime.text = "현재시간: " + formatter.string(from: date as Date) // as Date => NSDate 형태를 Date로 형변환 한다는 의미
        
        let currentTime = formatter.string(for: date)
        
        if(alarmTime == currentTime){
            //uiView.backgroundColor = UIColor.red
            let alarmAlert = UIAlertController(title: "알림", message: "설정된 시간입니다!!!", preferredStyle: UIAlertController.Style.alert)
                
            let alarmAction = UIAlertAction(title: "네, 알겠습니다.", style: UIAlertAction.Style.default, handler: {
                ACTION in self.isAlarmTime = true
            })
                
            if (isAlarmTime==false) {
                alarmAlert.addAction(alarmAction)
                present(alarmAlert, animated: true, completion: nil)
            }
        }
        else{
            //uiView.backgroundColor = UIColor.white
            isAlarmTime = false
        }
    }
    
    @IBAction func selectDateDone(_ sender: UIButton) {
        if delegate != nil {
            delegate?.didDatePickDone(self, date: pickerTime)
        }
        
        _ = navigationController?.popViewController(animated: true)
    }
}
