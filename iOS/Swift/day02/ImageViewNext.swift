import UIKit

class ViewController: UIViewController {

    @IBOutlet weak var imgView: UIImageView!
    
    var img: UIImage?
    var imgNum = 1
    var imgNamge = String(1) + ".png"
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        
        img = UIImage(named: imgNamge)
        imgView.image = img
    }
    
    @IBAction func btnBefore(_ sender: Any) {
        if(imgNum != 1){
            imgNum -= 1
            imgNamge = String(imgNum) + ".png"
        }
        else{
            imgNamge = String(1) + ".png"
        }
        
        img = UIImage(named: imgNamge)
        imgView.image = img
        
    }
    
    @IBAction func btnAfter(_ sender: Any) {
        if(imgNum != 6){
            imgNum += 1
            imgNamge = String(imgNum) + ".png"
        }
        else{
            imgNamge = String(6) + ".png"
        }
        
        img = UIImage(named: imgNamge)
        imgView.image = img
    }
    
}

