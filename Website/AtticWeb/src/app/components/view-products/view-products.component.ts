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


  header;
  path: string="";
  furnitures: IFurniture[] =this.firestore.getFurnitures();
  storeFurniture: IFurniture[] = [];

  notEmpty:boolean = false;


  constructor(private firestore: FirestoreService) {
    this.furnitures=this.firestore.getFurnitures();
   }

  ngOnInit() {
    this.header = {title: "VIEW PRODUCTS",account: this.user}
    
    console.log(this.furnitures)
    // if(this.furnitures != []){
      this.notEmpty =true;
      this.getStoreFurniture();
      console.log(this.storeFurniture);
    // }

  }

  getStoreFurniture(){
    this.furnitures.forEach(furniture=>{
      if(furniture.manufacturer == this.user.store){
        this.storeFurniture.push(furniture)
      }
    })
  }


}
