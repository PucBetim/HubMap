import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { DragDropModule } from '@angular/cdk/drag-drop';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatTooltipModule } from '@angular/material/tooltip';

import { CreationComponent } from './creation/creation.component';
import { CreatorRoutes } from './map-creator.routing';
import { FormsModule } from '@angular/forms';

@NgModule({
  declarations: [
    CreationComponent
  ],
  imports: [
    CommonModule,
    RouterModule.forChild(CreatorRoutes),
    DragDropModule,
    MatIconModule,
    MatButtonModule,
    MatTooltipModule,
    FormsModule
  ],
  providers:[
  ]
})
export class MapCreatorModule { }
