//
//  ViewController.swift
//  PinchGesture
//
//  Created by hyusnoo on 2022/07/07.
//

import UIKit

class ViewController: UIViewController {

    @IBOutlet var txtPinch: UILabel!
    @IBOutlet var imgPinch: UIImageView!
    
    var initialFontSize: CGFloat!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        
        let pinch = UIPinchGestureRecognizer(target: self, action: #selector(ViewController.doPinch(_:)))
        self.view.addGestureRecognizer(pinch)   // pinch gesture를 현재 뷰의 제스처에 등록하기
    }

    @objc func doPinch(_ pinch: UIPinchGestureRecognizer) {
        // 글자 pinch
       /* if (pinch.state == UIGestureRecognizer.State.began) {   // 화면에 닿았을 때
            initialFontSize = txtPinch.font.pointSize   // 현재의 글자 크기
        } else {    // 화면을 pinch 했을 때 (화면을 움직였을 때)
            txtPinch.font = txtPinch.font.withSize(initialFontSize * pinch.scale)   // pinch 만큼 글자 크기 변경
        }*/
        
        // 이미지 pinch
        imgPinch.transform = imgPinch.transform.scaledBy(x: pinch.scale, y: pinch.scale)
        pinch.scale = 1     // pinch가 증가,감소하는 크기 조정
    }

}
