import UIKit

class ViewController: UIViewController {

    @IBOutlet weak var lblHello: UILabel!
    @IBOutlet weak var txtName: UITextField!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
    }

    
    @IBAction func btnSend(_ sender: Any) {
        // !는 nil이 아닌 값이 무조건 할당되어야 한다는 뜻이다.
        lblHello.text = "Hello, " + txtName.text!
    }
}
