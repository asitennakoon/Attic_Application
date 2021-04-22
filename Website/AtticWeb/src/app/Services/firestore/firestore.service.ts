import { Injectable } from '@angular/core';
import { AngularFirestore } from '@angular/fire/firestore';
import { IFurniture } from 'src/app/Interfaces/IFurniture';

@Injectable({
  providedIn: 'root'
})
export class FirestoreService {
  /* this service is used to send and retrieve furniture data from firebase firestore.
  All the data are stored in the firestore in IFurniture type. */

  furnitures: IFurniture[]=[];
  scenes: string[] = [];
  types: string[] = [];

  constructor(private firestore: AngularFirestore) {


  }

// a method to get all the furnitures from firestore.
  getFurnitures() {
    // emptying the furniture array to avoid duplication.
    this.furnitures = [];
      // getting the relevant snapshot from the firebase and subscribe to it.
      this.firestore.collection<IFurniture>('furniture-info').snapshotChanges().subscribe(data => {
        // for each snapshot, there is a payload which contains the data;
        data.forEach(furnituresnap => {
          // get the data from the payload and typecast it to IFurniture type.
          let furniture = <IFurniture>furnituresnap.payload.doc.data();
          
          // the data of the payload doesn't contain the key.
          // therefore, must use the id method to get the key.
          if (furnituresnap.payload.doc.id) {
            furniture.$key = furnituresnap.payload.doc.id;
           
            // compare the furniture with all the furnitures in the furnitures array to avoid duplication.
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
    
 
        // also update the scene types and furniture types.
      this.getSceneTypes()
      this.getFurnitureTypes()
      

    });
    // console.log(this.furnitures)
    return this.furnitures;
  
    
  }

  // a method to get all the different scene types ==> Bedroom-Contemporary, Bedroom-Modern etc.
  getSceneTypes() {
    this.furnitures.forEach(furniture => {
      // if the scenes array doesn't contain the scene add it.
      if (!(this.scenes.includes(furniture.scene))) {
        this.scenes.push(furniture.scene);
      }

    });

    // return the scenes array.
    return this.scenes;
  }

  // a method to get all the different furniture types ==> chairs, tables etc.
  getFurnitureTypes() {
    this.furnitures.forEach(furniture => {
      // if the scenes array doesn't contain the furniture type add it.
      if (!(this.types.includes(furniture.type))) {
        this.types.push(furniture.type);
      }
    });

    // return the types array
    return this.types;
  }


  /* a method to get the exisiting type count of a scene (ex: nummber of chairs in Bedroom-Moden scene ).
  This method is used when adding a new furniture (ex: Bedroom-Modern-Chair2)

  When getting the type count it returns the highest number instead of the count because,
    when one furniture gets deleted, it will reduce the count by one and,
    when adding a furniture next, it will have a the new count and for that count, 
    there is an existing furniture.
  
  To prevent that we get the highest gven number and add 1 to it.  
  */ 
  getExistingCount(scene: string, type: string){

    //array to hold the furnitures of the specific scene
    let selectedFurnitures: IFurniture[]=[];
    console.log(this.furnitures)
    
    this.furnitures.forEach(furniture =>{
      if(furniture.scene== scene && furniture.type == type){
        selectedFurnitures.push(furniture);
      }
    });

    // if there are no furnitures of that type
    if(selectedFurnitures.length ==0){
      return 1;
    }

    /*  scene and type are separated by "-" which has the length of 1
     the $key contains the exact name in format of scene-type[type count]
    */
    let length: number = scene.length + 1 +type.length;
    console.log("length: "+length)

    var max: number = 0;
    selectedFurnitures.forEach(furniture =>{
      let num = furniture.$key.slice(length,furniture.$key.length);
      let num1 = num as unknown as number;
      if (num1> max){
        max= num1;
      }
    });
    // max is the highest number of furnitures of the scene and the type existing in the store.
    max=Number(max) + Number(1);
    console.log("max: "+max);
 
    return max;

  }



  // the method to add a furniture to firestore.
  addFurniture(key: string, furniture: IFurniture){
    this.firestore.collection('furniture-info').doc(key).set(furniture);
    // after adding the furniture, refresh the furniture arrays.
    this.getFurnitures();


  }

  // updateFurniture($key:string, furniture: IFurniture){
  //   this.firestore.collection('furniture-info').
  // }





  // a method to find whether two furnitures are equal.
  // if all the attributes are true, return true(equal) and else false(not equal)
  equals(f1: IFurniture, f2: IFurniture){
    if(f1.$key==f2.$key
      && f1.color == f2.color
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
