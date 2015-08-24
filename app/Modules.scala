package modules

import scaldi.Module
import services.SimpleUUIDGenerator


class UUIDModule extends Module {
  binding to new SimpleUUIDGenerator
}
