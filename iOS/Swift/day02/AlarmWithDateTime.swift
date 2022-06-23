import UIKit

class ViewController: UIViewController {
    
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
    
    @objc func updateTime() {
        let date = NSDate()
        
        let formatter = DateFormatter()
        formatter.dateFormat = "hh:mm:ss aaa"
        lblCurrentTime.text = "현재시간: " + formatter.string(from: date as Date) // as Date => NSDate 형태를 Date로 형변환 한다는 의미
        
        // 현재시간과 선택한 알람시간이 같으면 배경화면 색을 red로 변경한다.
        let currentTime = formatter.string(for: date)
        if(alarmTime == currentTime){
            uiView.backgroundColor = UIColor.red
        }
        else{
            uiView.backgroundColor = UIColor.white
        }
        
    }
    
}
