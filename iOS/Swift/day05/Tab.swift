import UIKit

class ViewController: UIViewController {

    @IBOutlet var imageView: UIImageView!
    let homeImg = "6.jpg"
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        imageView.image = UIImage(named: homeImg)
    }

    @IBAction func btnMoveMapView(_ sender: UIButton) {
        tabBarController?.selectedIndex = 3     // tabBar index에 해당하는 페이지로 이동한다.
    }
    
    @IBAction func btnMoveDatePickerView(_ sender: UIButton) {
        tabBarController?.selectedIndex = 2
    }
    
}
