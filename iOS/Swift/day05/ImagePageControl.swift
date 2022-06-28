@IBOutlet var imgView: UIImageView!
    @IBOutlet var pageControl: UIPageControl!
    
    var images = ["01.png", "02.png", "03.png", "04.png", "05.png", "06.png"]   // 이미지 파일명 배열
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        
        pageControl.numberOfPages = images.count    // page Control 개수 설정
        pageControl.currentPage = 0     // 초기 페이지 index 0으로 설정
        
        pageControl.pageIndicatorTintColor = UIColor.cyan  // page Control 기본 색상
        pageControl.currentPageIndicatorTintColor = UIColor.red     // 선택 색상
            
        imgView.image = UIImage(named: images[0])   // 초기 이미지 설정
    }
    
    @IBAction func pageChange(_ sender: UIPageControl) {
        imgView.image = UIImage(named: images[pageControl.currentPage]) // 현재페이지 인덱스를 이용해서 이미지 파일명을 배열에서 꺼내온다.
    }
