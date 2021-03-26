import { Injectable } from '@angular/core';
import { AngularFirestore } from '@angular/fire/firestore';
import { IFurniture } from 'src/app/Interfaces/IFurniture';

@Injectable({
  providedIn: 'root'
})
export class FirestoreService {

  furnitures: IFurniture[]=[];
  scenes: string[] = [];
  types: string[] = [];

  constructor(private firestore: AngularFirestore) {


  }


  getFurnitures() {
    this.furnitures = [];

      this.firestore.collection<IFurniture>('furniture-info').snapshotChanges().subscribe(data => {
        
        data.forEach(furnituresnap => {
          let furniture = <IFurniture>furnituresnap.payload.doc.data();
  
          if (furnituresnap.payload.doc.id) {
            furniture.$key = furnituresnap.payload.doc.id;
           
  
            let inTheList: boolean = false;
            this.furnitures.forEach(f =>{
              if(this.equals(f,furniture)){
                inTheList=true;
              }
            });
  
            if (!inTheList) {
              this.furnitures.push(furniture);
            }
          }
        });
    
 

      this.getSceneTypes()
      this.getFurnitureTypes()
      

    });
    // console.log(this.furnitures)
    return this.furnitures;
  
    
  }

  getSceneTypes() {
    this.furnitures.forEach(furniture => {

      if (!(this.scenes.includes(furniture.scene))) {
        this.scenes.push(furniture.scene);
      }

    });


    return this.scenes;
  }

  getFurnitureTypes() {
    this.furnitures.forEach(furniture => {
      if (!(this.types.includes(furniture.type))) {
        this.types.push(furniture.type);
      }
    });


    return this.types;
  }


  getExistingCount(scene: string, type: string){

    
    let selectedFurnitures: IFurniture[]=[];
    
    this.furnitures.forEach(furniture =>{
      if(furniture.scene== scene && furniture.type == type){
        selectedFurnitures.push(furniture);
      }
    });

    if(selectedFurnitures.length ==0){
      return 0;
    }

    let length: number = scene.length + 1 +type.length;

    var max: number = 0;
    selectedFurnitures.forEach(furniture =>{
      let num = furniture.$key.slice(length,furniture.$key.length);
      let num1 = num as unknown as number;
      if (num1> max){
        max= num1;
      }
    });

    max=Number(max) + Number(1);
 
    return max;

  }




  addFurniture(key: string, furniture: IFurniture){
    this.firestore.collection('furniture-info').doc(key).set(furniture);
    this.getFurnitures();

    console.log(this.furnitures)

  }

  // updateFurniture($key:string, furniture: IFurniture){
  //   this.firestore.collection('furniture-info').
  // }






  equals(f1: IFurniture, f2: IFurniture){
    if(f1.$key==f2.$key
      && f1.colour == f2.colour
      && f1.description == f2.description
      && f1.manufacturer == f2.manufacturer
      && f1.material == f2.material
      && f1.price == f2.price
      && f1.scene == f2.scene
      && f1.stock == f2.stock
      && f1.type == f2.type
      ){
        return true;
      }

      return false;
  }

}
