import { Component, Input, OnInit } from '@angular/core';
import { IAccount } from 'src/app/Interfaces/IAccount';
import { IFurniture } from 'src/app/Interfaces/IFurniture';
import { FirestoreService } from 'src/app/Services/firestore/firestore.service';

@Component({
  selector: 'app-view-products',
  templateUrl: './view-products.component.html',
  styleUrls: ['./view-products.component.css']
})
export class ViewProductsComponent implements OnInit {

  @Input() user!: IAccount;

  addProduct: boolean;
  header;
  path: string="";
  furnitures: IFurniture[] =this.firestore.getFurnitures();
  storeFurniture: IFurniture[] = []


  constructor(private firestore: FirestoreService) {
    this.furnitures=this.firestore.getFurnitures();
    
   }

  ngOnInit() {
    this.header = {title: "VIEW PRODUCTS",account: this.user}
    
    // console.log(this.furnitures)
      this.storeFurniture =this.getStoreFurniture();
      this.addProduct=false;
      // console.log(this.storeFurniture)


  }

  getStoreFurniture(){
    this.furnitures=this.firestore.getFurnitures();

    let furnitures1: IFurniture[] = [];
    // console.log(this.furnitures)
    let furnitures2: IFurniture[] = this.furnitures;

    // console.log(this.user.store)
    // console.log(furnitures2.length)

    let user = this.user;
    
    setTimeout(function(){
      for(let furniture of furnitures2){

        console.log("inside for")
        if(furniture.manufacturer == user.store){
          furnitures1.push(furniture);
          console.log(furniture);
        }
      };
    }, 500);
    

    return furnitures1;

  }


  onAddProductClick(){
    this.addProduct = true;
  }


}
