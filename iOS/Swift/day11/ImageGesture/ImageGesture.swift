//
//  ViewController.swift
//  ImageGesture
//
//  Created by Dongduk4 on 2022/07/07.
//

import UIKit

class ViewController: UIViewController {

    @IBOutlet var imgView: UIImageView!
    @IBOutlet var pageControl: UIPageControl!
    @IBOutlet var rotationRecogized: UIRotationGestureRecognizer!
    
    var images = ["blueRabbit.jpeg", "rabbit.jpeg", "cuteRabbit.jpeg"]
    var currentPage = 0
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        
        // 페이지 컨트롤
        pageControl.numberOfPages = images.count
        pageControl.currentPage = 0
        
        pageControl.pageIndicatorTintColor = UIColor.blue
        pageControl.currentPageIndicatorTintColor = UIColor.magenta
        
        imgView.image = UIImage(named: images[0])
        
        // 스와이프
        let swipeRight = UISwipeGestureRecognizer(target: self, action: #selector(ViewController.respondToSwipeGesture(_:)))
        swipeRight.direction = UISwipeGestureRecognizer.Direction.right
        self.view.addGestureRecognizer(swipeRight)
        
        let swipeLeft = UISwipeGestureRecognizer(target: self, action: #selector(ViewController.respondToSwipeGesture(_:)))
        swipeLeft.direction = UISwipeGestureRecognizer.Direction.left
        self.view.addGestureRecognizer(swipeLeft)
        
        // 핀치
        let pinch = UIPinchGestureRecognizer(target: self, action: #selector(ViewController.doPinch(_:)))
        self.view.addGestureRecognizer(pinch)
        
        // 핀치 회전
        let rotationRecogniezer = UIRotationGestureRecognizer(target: self, action: #selector(rotationAction(_:)))
        self.view.addGestureRecognizer(rotationRecogniezer)
    }

    @IBAction func pageChange(_ sender: UIPageControl) {
        imgView.image = UIImage(named: images[pageControl.currentPage])
    }
    
    @objc func rotationAction(_ pinch: UIRotationGestureRecognizer) {
        imgView.transform = imgView.transform.rotated(by: pinch.rotation)
        pinch.rotation = 0.0
    }
    
    @objc func respondToSwipeGesture(_ gesture: UIGestureRecognizer) {
        if let swipeGesture = gesture as? UISwipeGestureRecognizer {
            switch swipeGesture.direction {
            case UISwipeGestureRecognizer.Direction.left :
                if currentPage != 2 {
                    currentPage += 1
                }
                imgView.image = UIImage(named: images[currentPage])
            case UISwipeGestureRecognizer.Direction.right :
                if currentPage != 0 {
                    currentPage -= 1
                }
                imgView.image = UIImage(named: images[currentPage])
            default:
                break
            }
            pageControl.currentPage = currentPage
        }
    }
    
    @objc func doPinch(_ pinch: UIPinchGestureRecognizer) {
        imgView.transform = imgView.transform.scaledBy(x: pinch.scale, y: pinch.scale)
        pinch.scale = 1
    }
    
}

