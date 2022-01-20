<template>
  <p v-if="$fetchState.pending">Fetching programmes...</p>
  <p v-else-if="$fetchState.error">An error occurred :(</p>
  <div v-else>
    <h1>Programmes</h1>
      <v-data-table
      :headers="headers"
      :items="programme"
      :items-per-page="15"
      class="elevation-1"
      ></v-data-table>
      <v-spacer></v-spacer>
  </div>
</template>
<script>
  export default {
    data() {
      return {
        programme: [],
        headers: [
          {text: 'ID', value: 'id'},
          {text: 'Title', value: 'title'},
          {text: 'Playout Cost', value: 'playoutCost'},
          {text: 'Discountable', value: 'isDiscountable'}
          
        ]
      }
    },
    async fetch() {
      this.programme = await fetch(
        'http://localhost:3000/itv/programme'
      ).then(res => res.json())
    }
  }
</script>