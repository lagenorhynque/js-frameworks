import { mapGetters, mapActions } from 'vuex'
import {
  GET_CHANNELS
} from '../../store/mutation-types'
import MessageList from '../MessageList'

export default {
  name: 'chat',
  components: {
    'message-list': MessageList
  },
  mounted() {
    this.GET_CHANNELS()
    this.GET_MESSAGES(this.$route.params.channelId)
  },
  watch: {
    '$route.params.channelId': function (channelId) {
      this.GET_MESSAGES(channelId)
    }
  },
  computed: {
    ...mapGetters([
      'messages',
      'channels'
    ])
  },
  methods: {
    ...mapActions([
      GET_CHANNELS,
      'GET_MESSAGES',
      'POST_MESSAGES'
    ]),
    sendMessage() {
      this.POST_MESSAGES({
        channelId: this.$route.params.channelId,
        message: this.message
      })
      this.message = ''
    }
  },
  data() {
    return {
      message: ''
    }
  }
}
