<template>
  <div id="app">
    <router-view/>
  </div>
</template>

<script>
  import Driver from 'driver.js' // import driver.js
  import 'driver.js/dist/driver.min.css' // import driver.js css
  import steps from './views/guide/steps'
  export default {
    name: 'App',
    data() {
      return {
        driver: null
      }
    },
    mounted() {
      console.log("this ia app.vue")
      this.driver = new Driver()
      const that = this;
      if (!localStorage.getItem("guide_key")) {
        that.driver.defineSteps(steps)
        setTimeout(function () {
          that.driver.start()
        }, 1000)
        localStorage.setItem("guide_key","finish")
      }
    },
  }
</script>
