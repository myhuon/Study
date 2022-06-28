import UIKit

class ViewController: UIViewController {

    @IBOutlet var lblNumber: UILabel!
    @IBOutlet var pageControl: UIPageControl!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        
        pageControl.numberOfPages = 10     // page Control 개수 설정
        pageControl.currentPage = 0     // 초기 페이지 index 0으로 설정
                
        pageControl.pageIndicatorTintColor = UIColor.cyan  // page Control 기본 색상
        pageControl.currentPageIndicatorTintColor = UIColor.red     // 선택 색상
                    
        lblNumber.text = "1"
    }

    @IBAction func pageChange(_ sender: UIPageControl) {
        lblNumber.text = String(pageControl.currentPage + 1)
    }
    
}
