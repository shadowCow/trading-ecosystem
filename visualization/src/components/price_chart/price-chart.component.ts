import {Component} from 'angular2/core';
import {PriceBarComponent} from './price_bar/price-bar.component';

@Component({
  selector: 'price-chart',
  templateUrl: '/dist/components/price_chart/price-chart.html',
  styleUrls: ['/dist/components/price_chart/price-chart.css'],
  directives: [PriceBarComponent]
})
export class PriceChartComponent {

}
