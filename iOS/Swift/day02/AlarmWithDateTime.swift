import UIKit

class ViewController: UIViewController {
    
    /*
        updateTime 함수를 selector로 감싸서 시스템에 callback 함수로 넘겨준다.
     */
    let timeSelector: Selector = #selector(ViewController.updateTime)
    let interval = 1.0
    var count = 0
    var alarmTime = "0"

    @IBOutlet var uiView: UIView!
    @IBOutlet weak var lblCurrentTime: UILabel!
    @IBOutlet weak var lblPickerTime: UILabel!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
     
        Timer.scheduledTimer(timeInterval: interval, target: self, selector: timeSelector, userInfo: nil, repeats: true)
    }
    
    @IBAction func changeDatePicker(_ sender: UIDatePicker) {
        let datePickerView = sender
        
        let formatter = DateFormatter()
        formatter.dateFormat = "hh:mm:ss aaa"
        lblPickerTime.text = "선택시간: " + formatter.string(from: datePickerView.date)
    
        // 알람기능
        alarmTime = formatter.string(from: datePickerView.date)
    }
    
    // callback 함수, @objc는 objective-c를 사용하겠다는 의미.
    @objc func updateTime() {
        let date = NSDate()
        
        let formatter = DateFormatter()
        formatter.dateFormat = "hh:mm:ss aaa"
        lblCurrentTime.text = "현재시간: " + formatter.string(from: date as Date) // as Date => NSDate 형태를 Date로 형변환 한다는 의미
        
        let currentTime = formatter.string(for: date)
        if(alarmTime == currentTime){
            uiView.backgroundColor = UIColor.red
        }
        else{
            uiView.backgroundColor = UIColor.white
        }
        
    }
    
}
