//
//  ViewController.swift
//  ImageView
//
//  Created by Dongduk4 on 2022/06/23.
//

import UIKit

class ViewController: UIViewController {

    var isZoom = false
    var imgOn: UIImage?
    var imgOff: UIImage?
    
    @IBOutlet weak var imgView: UIImageView!
    @IBOutlet weak var btnResize: UIButton!
    
    // app 실행 시 자동으로 호출되는 함수
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        
        imgOn = UIImage(named: "lamp_on.png")
        imgOff = UIImage(named: "lamp_off.png")
        
        imgView.image = imgOn
    }

    // 32bit -> float, 64bit -> double / 기본 자료형 보강 => CGFloat
    @IBAction func btnResizeImage(_ sender: Any) {
        let scale: CGFloat = 2.0 // let은 상수를 선언할 때 사용함
        var newWidth: CGFloat, newHeight: CGFloat
        
        if(isZoom){
            newWidth = imgView.frame.width / scale
            newHeight = imgView.frame.height / scale
            
            btnResize.setTitle("확대", for: .normal)
        }
        else{
            newWidth = imgView.frame.width * scale
            newHeight = imgView.frame.height * scale
            
            btnResize.setTitle("축소", for: .normal)
        }
        
        // CGSize는 메모리 고려하지 않고 저장 가능
        imgView.frame.size = CGSize(width: newWidth, height: newHeight)
        
        isZoom = !isZoom
    }
    
    @IBAction func swiftOnOff(_ sender: UISwitch) {
        if sender.isOn {
            imgView.image = imgOn
        }
        else{
            imgView.image = imgOff
        }
    }
}

