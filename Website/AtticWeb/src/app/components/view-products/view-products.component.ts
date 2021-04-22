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
  /*
    A cmponent to view the existing products.
  */

    // input decorator to get the relevant user account from the login component.
  @Input() user!: IAccount;

  addProduct: boolean;
  header;
  path: string="";
  furnitures: IFurniture[] =this.firestore.getFurnitures();
  storeFurniture: IFurniture[] = []


  constructor(private firestore: FirestoreService) {
    // initialise the furnitures
    this.furnitures=this.firestore.getFurnitures();
    
   }

  ngOnInit() {
    // initialise the header component.
    this.header = {title: "VIEW PRODUCTS",account: this.user}
    
    // get only the furitures belong to the user.
    // console.log(this.furnitures)
      this.storeFurniture =this.getStoreFurniture();
      this.addProduct=false;
      // console.log(this.storeFurniture)


  }

  // a method to get all the furnitures belong to the user.
  getStoreFurniture(){
    // get all the furnitures from the firestore service.
    this.furnitures=this.firestore.getFurnitures();

    let furnitures1: IFurniture[] = [];
    // console.log(this.furnitures)
    let furnitures2: IFurniture[] = this.furnitures;

    // console.log(this.user.store)
    // console.log(furnitures2.length)

    let user = this.user;
    // a timeout until the furnitures2 array is loaded.
    setTimeout(function(){
      for(let furniture of furnitures2){

        if(furniture.manufacturer == user.store){
          furnitures1.push(furniture);
          console.log(furniture);
        }
      };
    }, 500);
    

    return furnitures1;

  }

  // a flag to load the file-upload component.
  onAddProductClick(){
    this.addProduct = true;
  }


}
